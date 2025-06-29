CREATE Table customer(
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NUll,
    email TEXT NOT NULL,
    age INT NOT NULL,
    gender BOOLEAN NOT NULL
)