<?php
require_once "ApiService.php";

class Usuario extends ApiServices
{

    public function logar($email, $senha_hash)
    {
        return $this->request('/login', 'POST', [
            'email' => $email,
            'senha_hash' => $senha_hash
        ]);
    }

    public function cadastrar($nome, $email, $senha_hash, $curso_id)
    {
        return $this->request('/usuarios', 'POST', [
            'foto_url' => 'images/ft_perfil.webp',
            'nome' => $nome,
            'email' => $email,
            'senha_hash' => $senha_hash,
            'curso_id' => $curso_id
        ]);
    }
}