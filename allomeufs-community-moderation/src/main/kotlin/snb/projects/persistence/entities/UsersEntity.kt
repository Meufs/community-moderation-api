package com.templates.persistence.entities

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_generator")
    @SequenceGenerator(name = "users_generator", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "id", updatable = false)
    var id: Long? = null
    @Column(name = "first_name", columnDefinition = "varchar(30)", nullable = false)
    var firstName: String? = null
    @Column(name = "last_name", columnDefinition = "varchar(30)", nullable = false)
    var lastName: String? = null
    @Column(name = "mail", columnDefinition = "varchar(90)", nullable = false, unique = true)
    var mail: String? = null
    @Column(name = "password", columnDefinition = "bpchar(60)", nullable = false)
    var password: String? = null
    @Column(name = "reference", columnDefinition = "bpchar(32)", nullable = false, unique = true, updatable = false)
    var reference: String? = null
    @Column(name = "type", columnDefinition = "varchar(10)", nullable = false, updatable = false, insertable = false)
    var type: String? = null
    @Column(name = "phone_number", columnDefinition = "bpchar(10)", unique = true, nullable = false)
    var phoneNumber: String? = null
    @Column(name = "verification_code", columnDefinition = "varchar(100)")
    var verificationCode: String? = null
    @Column(name = "verification_code_timestamp", columnDefinition = "TIMESTAMP")
    var verificationCodeTimestamp: Timestamp? = null
    @Column(name = "password_verification_code", columnDefinition = "varchar(100)")
    var passwordVerificationCode: String? = null
    @Column(name = "password_verification_timestamp", columnDefinition = "TIMESTAMP")
    var passwordVerificationTimestamp: Timestamp? = null
    @Column(name = "profile_pic_url", columnDefinition = "varchar(200)")
    var profilePicture: String? = null
    @Column(name = "account_verified", columnDefinition = "boolean DEFAULT false", nullable = false)
    var accountVerifiedStatus: Boolean? = false
    override fun toString(): String {
        return "UsersEntity(id=$id, firstName=$firstName, lastName=$lastName, mail=$mail, password=$password, reference=$reference, type=$type, phoneNumber=$phoneNumber, verificationCode=$verificationCode, verificationCodeTimestamp=$verificationCodeTimestamp, passwordVerificationCode=$passwordVerificationCode, passwordVerificationTimestamp=$passwordVerificationTimestamp, profilePicture=$profilePicture, accountVerifiedStatus=$accountVerifiedStatus)"
    }


}