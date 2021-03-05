package com.example.storage.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.storage.modules.sys.entity.MailValidate;

@Mapper
public interface MailValidateDao {
    Integer insertMailValidate(MailValidate mailValidate);

    MailValidate selectMailValidateByEmail(@Param("targetMail") String targetMail);

    void updateMailValidateByEmail(MailValidate mailValidate);

    void updateMailValidateStateByEmail(MailValidate mailValidate);

    void updateUseremail(@Param("oldEmail") String oldEmail, @Param("newEmail") String newEmail);
}
