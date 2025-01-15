//package com.sj.ecommerce.enums;
//
//public enum RoleEnum {
//    USER("ROLE_USER"),
//    ADMIN("ROLE_ADMIN");
//
//    private final String roleName;
//
//    RoleEnum(String roleName) {
//        this.roleName = roleName;
//    }
//
//    // Get the role name with the "ROLE_" prefix
//    public String getRoleName() {
//        return roleName;
//    }
//
//    // Get the role name without the "ROLE_" prefix (just the role name like "USER", "ADMIN")
//    public String getRole() {
//        return this.name();
//    }
//
//    // Static method to get RoleEnum from a string (removes "ROLE_" prefix before calling valueOf)
//    public static RoleEnum fromString(String roleName) {
//        for (RoleEnum role : RoleEnum.values()) {
//            if (role.getRoleName().equalsIgnoreCase(roleName)) {
//                return role;
//            }
//        }
//        throw new IllegalArgumentException("No enum constant with role name " + roleName);
//    }
//}
