CREATE TABLE IF NOT EXISTS users
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Insert data into the table
INSERT INTO users(email, password) VALUES ('test@gmail.com', 'password');
INSERT INTO users(email, password) VALUES ('ken.k@gmail.com', 'oldpassword');
INSERT INTO users(email, password) VALUES ('ken.k@yahoo.fr', 'kenyahoo');
