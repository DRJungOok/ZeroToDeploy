SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS chat_room_participant;
DROP TABLE IF EXISTS chat_room;

DROP TABLE IF EXISTS friend_request;
DROP TABLE IF EXISTS friend;

DROP TABLE IF EXISTS post_like;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS post;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS role;

SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255),
                       provider VARCHAR(20),         -- KAKAO, GOOGLE 등
                       provider_id VARCHAR(255),     -- OAuth2 계정 고유 ID
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE user_role (
                           user_id BIGINT NOT NULL,
                           role_id BIGINT NOT NULL,
                           PRIMARY KEY(user_id, role_id),
                           FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
                           FOREIGN KEY(role_id) REFERENCES role(id) ON DELETE CASCADE
);


CREATE TABLE post (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      user_id BIGINT NOT NULL,
                      title VARCHAR(255) NOT NULL,
                      content LONGTEXT NOT NULL,
                      category VARCHAR(50),   -- /javaSpring, /web, /linux 등
                      view_count INT DEFAULT 0,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                      FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE comment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         post_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         content VARCHAR(1000) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         FOREIGN KEY(post_id) REFERENCES post(id) ON DELETE CASCADE,
                         FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE post_like (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           post_id BIGINT NOT NULL,
                           user_id BIGINT NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           UNIQUE(post_id, user_id),

                           FOREIGN KEY(post_id) REFERENCES post(id) ON DELETE CASCADE,
                           FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE friend (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        friend_id BIGINT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                        UNIQUE(user_id, friend_id),

                        FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
                        FOREIGN KEY(friend_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE friend_request (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                sender_id BIGINT NOT NULL,
                                receiver_id BIGINT NOT NULL,
                                status VARCHAR(20) DEFAULT 'PENDING',  -- PENDING / ACCEPTED / REJECTED
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                UNIQUE(sender_id, receiver_id),

                                FOREIGN KEY(sender_id) REFERENCES users(id) ON DELETE CASCADE,
                                FOREIGN KEY(receiver_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE chat_room (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           room_name VARCHAR(255),
                           type VARCHAR(20) NOT NULL  -- DIRECT or GROUP
);


CREATE TABLE chat_room_participant (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       room_id BIGINT NOT NULL,
                                       user_id BIGINT NOT NULL,

                                       UNIQUE(room_id, user_id),

                                       FOREIGN KEY(room_id) REFERENCES chat_room(id) ON DELETE CASCADE,
                                       FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE message (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         room_id BIGINT NOT NULL,
                         sender_id BIGINT NOT NULL,
                         content VARCHAR(2000) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         FOREIGN KEY(room_id) REFERENCES chat_room(id) ON DELETE CASCADE,
                         FOREIGN KEY(sender_id) REFERENCES users(id) ON DELETE CASCADE
);
