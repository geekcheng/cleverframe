package org.cleverframe.sys.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.cleverframe.common.codec.EncodeDecodeUtils;
import org.cleverframe.sys.SysBeanNames;
import org.cleverframe.sys.entity.Organization;
import org.cleverframe.sys.entity.Resources;
import org.cleverframe.sys.entity.Role;
import org.cleverframe.sys.entity.User;
import org.cleverframe.sys.service.AuthorizingRealmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016/11/8 22:52 <br/>
 */
public class UserAuthorizingRealm extends AuthorizingRealm {
    /**
     * 日志对象
     */
    private final static Logger logger = LoggerFactory.getLogger(UserAuthorizingRealm.class);

    @Autowired
    @Qualifier(SysBeanNames.AuthorizingRealmService)
    public AuthorizingRealmService authorizingRealmService;

    /**
     * Shiro认证时调用，到数据库获取认证信息<br>
     * 1.此方法的返回值AuthenticationInfo，当前对象的credentialsMatcher属性对象会使用此对象进行验证<br>
     * 2.当前对象的credentialsMatcher属性值在Spring注入是指定，其默认值：SimpleCredentialsMatcher<br>
     * 3.注意：在使用缓存的情况下，此方法并不是每次认证时都调用<br>
     *
     * @param token 用户提交的身份信息，如：用户名、密码
     * @return 返回Shiro的认证信息 AuthenticationInfo
     * @see org.apache.shiro.authc.credential.SimpleCredentialsMatcher
     * @see org.apache.shiro.authc.credential.HashedCredentialsMatcher
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UserLoginToken userToken;
        if (token instanceof UserLoginToken) {
            userToken = (UserLoginToken) token;
        } else {
            return null;
        }
        if (StringUtils.isBlank(userToken.getUsername()) || userToken.getPassword() == null) {
            throw new UserLoginException(UserLoginException.UserName_Password_Is_Empty, "用户名、密码不能为空");
        }

        // 获取用户信息，此处不用验证用户名密码是否正确，验证过程由AuthenticationInfo完成
        User user = authorizingRealmService.getUserByLoginName(userToken.getUsername());
        if (user == null) {
            throw new UserLoginException(UserLoginException.Account_Not_Exists, "帐号不存在");
        } else if (!User.DEL_FLAG_NORMAL.equals(user.getDelFlag()) || User.ACCOUNT_STATE_DELETE.equals(user.getUserState())) {
            throw new UserLoginException(UserLoginException.Account_Delete, "此帐号已被删除");
        } else if (User.ACCOUNT_STATE_LOCKED.equals(user.getAccountState())) {
            throw new UserLoginException(UserLoginException.Account_Locked, "你的帐号已被锁定");
        } else if (User.USER_STATE_LEAVE.equals(user.getUserState())) {
            throw new UserLoginException(UserLoginException.User_Leave, "离职员工不能登录");
        }

        // 获取用户组织机构信息 - TODO 不存在阻止用户登录？
        Organization homeCompany = authorizingRealmService.getHomeCompany(user);
        Organization homeOrg = authorizingRealmService.getHomeOrg(user);
        // Password组成：前16存储密码的salt，16以后存储密码
        byte[] salt = EncodeDecodeUtils.decodeHex(user.getPassword().substring(0, 16));
        return new SimpleAuthenticationInfo(
                new UserPrincipal(homeCompany, homeOrg, user),// 用户信息
                user.getPassword().substring(16),// 密码
                new SaltByteSource(salt),// salt
                getName()// Realm name
        );
    }

    /**
     * Shiro授权时调用的方法，到数据库获取获取授权信息<br>
     * 1.因为Shiro中可以同时配置多个Realm，所以身份信息可能就有多个，因此其提供了PrincipalCollection用于聚合这些身份信息<br>
     * 2.可参考 org.apache.shiro.realm.jdbc.JdbcRealm 获取所有角色信息，所有权限信息<br>
     * 3.注意：在使用缓存的情况下，此方法并不是每次认证时都调用<br>
     *
     * @param principals 身份信息
     * @return 返回Shiro的授权信息 AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object object = getAvailablePrincipal(principals);
        if (object == null) {
            logger.error("### 授权失败，不能获取当前用户[Principal]");
            return null;
        }
        if (!(object instanceof UserPrincipal)) {
            logger.error("### 授权失败，用户对象不能转换成UserPrincipal对象");
            return null;
        }
        UserPrincipal principal = (UserPrincipal) object;
        if (principal.getUser() == null) {
            logger.error("### 授权失败，不能获取当前登录用户[User]");
            return null;
        }

        // 创建授权信息对象
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = principal.getUser();
        List<Role> roleList = authorizingRealmService.findRoleByUser(user);
        for (Role role : roleList) {
            authorizationInfo.addRole(role.getName());
        }
        List<Resources> resourcesList = authorizingRealmService.findResourcesByUser(user);
        for (Resources resources : resourcesList) {
            authorizationInfo.addStringPermission(resources.getPermission());
        }
        logger.info("用户[{}]，授权成功，用户角色 {} 个，系统资源 {} 个", user.getLoginName(), roleList.size(), resourcesList.size());
        return authorizationInfo;
    }

//    clearCachedAuthenticationInfo - 清除缓存 认证信息

//    clearCachedAuthorizationInfo - 清除缓存 授权信息

//    clearCache - - 清除缓存 认证信息 授权信息

//    clearAllCachedAuthorizationInfo、clearAllCachedAuthenticationInfo、clearAllCache
}
