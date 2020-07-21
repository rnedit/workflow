package kz.spring.workflow.domain;


public enum ERole {
        ROLE_USER(1,"ROLE_USER"),
        ROLE_MODERATOR(2,"ROLE_MODERATOR"),
        ROLE_ADMIN(3,"ROLE_ADMIN"),

        ROLE_ANONYMOUS(4,"ROLE_ANONYMOUS");

        ERole(int i, String role_user) {
        }

}

