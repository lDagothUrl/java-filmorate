CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    user_name CHARACTER VARYING(200),
    user_email CHARACTER VARYING(300) NOT NULL UNIQUE,
    user_login CHARACTER VARYING(200) NOT NULL UNIQUE,
    user_birthday DATE
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
    rating_id SERIAL PRIMARY KEY,
    rating CHARACTER VARYING(15) NOT NULL,
    description CHARACTER VARYING(200)
);

CREATE TABLE IF NOT EXISTS films (
    film_id integer not null primary key auto_increment,
     name CHARACTER VARYING(200) NOT NULL,
    description CHARACTER VARYING(300),
    release_date DATE,
    duration INTEGER,
    mpa_id INTEGER,

    CONSTRAINT films_mpa_fk FOREIGN KEY (mpa_id) REFERENCES mpa_ratings(rating_id)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id SERIAL PRIMARY KEY,
    genre_name CHARACTER VARYING(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_genres (
    film_id INT NOT NULL,
    genre_id INT NOT NULL,

    PRIMARY KEY(film_id, genre_id),
    CONSTRAINT films_id_fk FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    CONSTRAINT genre_id_fk FOREIGN KEY (genre_id) REFERENCES genres(genre_id)
);

CREATE TABLE IF NOT EXISTS films_likes (
    film_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY(film_id, user_id),
    CONSTRAINT fk_films_id FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friend_list (
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    confirmed BOOLEAN,

    PRIMARY KEY(user_id, friend_id),
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT friend_id_fk FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE RESTRICT
);