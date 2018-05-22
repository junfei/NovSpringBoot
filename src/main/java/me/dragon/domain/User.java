package me.dragon.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by dragon on 11/4/2017.
 */
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;
}
