package com.rbac.service;

import java.util.Set;

/**
 * Central authorization service.
 * // OOP: Abstraction – defines contract via interface.
 * // OOP: Collections – works with Set of permission strings.
 */
public interface AuthorizationService {

    /**
     * Returns true if the given user has the specified permission.
     * // OOP: Polymorphism – implementations can provide different strategies.
     */
    boolean hasPermission(int userId, String permission);

    /**
     * Returns the effective set of permission names for the user.
     */
    Set<String> getEffectivePermissions(int userId);
}
