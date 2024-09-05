INSERT INTO `adresses` (`id`, `city`, `number`, `street`, `uf`, `zip_code`)
VALUES
(400, 'São Paulo', 123, 'Rua das Flores', 'SP', '01000-000'),
(500, 'Rio de Janeiro', 456, 'Avenida Atlântica', 'RJ', '22010-000'),
(600, 'Belo Horizonte', 789, 'Praça Sete de Setembro', 'MG', '30110-000');

INSERT INTO `users` (`id`, `birth_date`, `cpf`, `name`, `password`, `phone`, `role`, `username`, `verification_code`, `id_address`)
VALUES
(400, '1990-01-01', '08422365004', 'João Silva', '$2a$12$0mTx/SSBjfscwepDDBuJ3uefaDiz8DUjgKlAOAtoV1YbLcOFmh.a6', '11987654321', 'ROLE_ADMIN', 'joao@email.com', null, 400),
(500, '1985-05-15', '58793541090', 'Maria Oliveira', '$2a$12$0mTx/SSBjfscwepDDBuJ3uefaDiz8DUjgKlAOAtoV1YbLcOFmh.a6', '21998765432', 'ROLE_CLIENT', 'maria@email.com', null, 500),
(600, '1992-08-30', '34861623079', 'Carlos Pereira', '$2a$12$0mTx/SSBjfscwepDDBuJ3uefaDiz8DUjgKlAOAtoV1YbLcOFmh.a6', '31987654321', 'ROLE_CLIENT', 'carlos@email.com', null, 600);

INSERT INTO `accounts` (`id`, `value_account`, `id_user`)
VALUES
(400, 0, 400),
(500, 0, 500),
(600, 0, 600);