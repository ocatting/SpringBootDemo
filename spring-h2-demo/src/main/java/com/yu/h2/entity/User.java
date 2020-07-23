package com.yu.h2.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Entity
@Data
@Table(name="USER_INF")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String name;
    @Column
    private String sex;
}
