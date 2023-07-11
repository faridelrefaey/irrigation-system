CREATE TABLE IF NOT EXISTS land(
    id INT NOT NULL AUTO_INCREMENT,
    seed_type VARCHAR(50) NOT NULL,
    land_name VARCHAR(50) NOT NULL,
    area FLOAT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS irrigation_configuration(
    id INT NOT NULL AUTO_INCREMENT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    times_to_water_during_interval INT NOT NULL,
    water_amount FLOAT NOT NULL,
    land_id INT NOT NULL,
    sensor_id INT DEFAULT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS sensor(
    id INT NOT NULL AUTO_INCREMENT,
    sensor_name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS irrigation_period(
    id INT NOT NULL AUTO_INCREMENT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP DEFAULT NULL,
    is_successful BIT DEFAULT 0,
    irrigation_configuration_id INT,
    PRIMARY KEY(id)
);

ALTER TABLE irrigation_configuration ADD FOREIGN KEY (land_id) REFERENCES land(id);
ALTER TABLE irrigation_configuration ADD FOREIGN KEY (sensor_id) REFERENCES sensor(id);
ALTER TABLE irrigation_period ADD FOREIGN KEY (irrigation_configuration_id) REFERENCES irrigation_configuration(id);