CREATE TABLE IF NOT EXISTS "rating" (
  "id" integer PRIMARY KEY,
  "rating_name" varchar(10)
);

CREATE TABLE IF NOT EXISTS "films" (
  "id" integer PRIMARY KEY,
  "name" varchar(50),
  "description" varchar(200),
  "releaseDate" date,
  "duration" integer,
  "rating_id" integer,
  FOREIGN KEY ("rating_id") REFERENCES "rating" ("id")
);

CREATE TABLE IF NOT EXISTS "users" (
  "id" integer PRIMARY KEY,
  "email" varchar(50),
  "login" varchar(50),
  "name" varchar(50),
  "birthday" date
);

CREATE TABLE IF NOT EXISTS "likes_films" (
  "film_id" integer,
  "user_id" integer,
  PRIMARY KEY ("film_id", "user_id"),
  FOREIGN KEY ("film_id") REFERENCES "films" ("id"),
  FOREIGN KEY ("user_id") REFERENCES "users" ("id")
);

CREATE TABLE IF NOT EXISTS "friends" (
  "user_id" integer,
  "friend_id" integer,
  "status" boolean,
  PRIMARY KEY ("user_id", "friend_id"),
  FOREIGN KEY ("user_id") REFERENCES "users" ("id"),
  FOREIGN KEY ("friend_id") REFERENCES "users" ("id")
);

CREATE TABLE IF NOT EXISTS "genres" (
  "id" integer PRIMARY KEY,
  "name" varchar(50)
);

CREATE TABLE IF NOT EXISTS "genre_film" (
  "film_id" integer,
  "genre_id" integer,
  FOREIGN KEY ("film_id") REFERENCES "films" ("id"),
  FOREIGN KEY ("genre_id") REFERENCES "genres" ("id")
);
