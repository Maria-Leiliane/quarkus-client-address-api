-- Tabela de Clientes
-- Define a estrutura principal para armazenar os dados dos clientes.
CREATE TABLE clients (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         document VARCHAR(14) UNIQUE NOT NULL,
                         document_type VARCHAR(4) NOT NULL, -- Será 'CPF' ou 'CNPJ', determinado pelo serviço.
                         phone VARCHAR(20),
                         email VARCHAR(150) UNIQUE NOT NULL,
                         password VARCHAR(100) NOT NULL, -- Tamanho adequado para armazenar senhas com hash.
                         created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- Tabela de Endereços
-- Armazena os endereços, com uma chave estrangeira (foreign key) que os liga a um cliente.
CREATE TABLE addresses (
                           id BIGSERIAL PRIMARY KEY,
                           client_id BIGINT NOT NULL,
                           name VARCHAR(100) NOT NULL, -- Ex: "Casa", "Trabalho"
                           street VARCHAR(100) NOT NULL,
                           number VARCHAR(20) NOT NULL,
                           complement VARCHAR(100),
                           district VARCHAR(100) NOT NULL,
                           city VARCHAR(100) NOT NULL,
                           state VARCHAR(50) NOT NULL,
                           zip_code VARCHAR(20) NOT NULL,
                           main_address BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- A constraint que garante a integridade: se um cliente for deletado,
    -- todos os seus endereços também serão (ON DELETE CASCADE).
                           CONSTRAINT fk_client FOREIGN KEY(client_id) REFERENCES clients(id) ON DELETE CASCADE
);