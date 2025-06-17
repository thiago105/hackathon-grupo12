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

    public function listarUsuario($id){
        // 1. Constrói o endpoint dinamicamente, por exemplo: /usuarios/15
        $endpoint = '/usuarios/' . $id;

        // 2. Faz uma requisição do tipo GET para esse endpoint.
        //    Requisições GET geralmente não enviam um corpo (body), por isso o terceiro parâmetro é nulo.
        return $this->request($endpoint, 'GET', []);
    

    }

    public function cadastrar($nome, $email, $senha_hash, $confirmarSenha_hash, $curso_id)
    {
        return $this->request('/usuarios', 'POST', [
            'foto_url' => 'images/ft_perfil.webp',
            'nome' => $nome,
            'email' => $email,
            'senha_hash' => $senha_hash,
            'confirmarSenha_hash' => $confirmarSenha_hash,
            'curso_id' => $curso_id
        ]);
    }
}