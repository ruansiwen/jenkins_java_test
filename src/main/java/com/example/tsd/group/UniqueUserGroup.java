package com.example.tsd.group;

import jakarta.validation.groups.Default;

/**
 * 自定义校验器的分组
 */
public interface UniqueUserGroup extends Default {

    interface Crud extends UniqueUserGroup {
        interface Create extends Crud {
        }
        interface Update extends Crud {
        }
    }
}
