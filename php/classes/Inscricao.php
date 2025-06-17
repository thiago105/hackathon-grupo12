<?php
require_once "ApiService.php";

class Inscricao extends ApiServices
{
    public function criarInscricao($usuario_id, $evento_id)
    {
        return $this->request('/inscricoes', 'POST', [
            'usuario_id' => $usuario_id,
            'evento_id' => $evento_id
        ]);
    }

    public function getInscricoesUsuario($usuario_id)
    {

    $url = $this->baseUrl . "/inscricoes/usuario/$usuario_id";

    $response = file_get_contents($url);

    if ($response === false) {
        return [];
    }
    
    $data = json_decode($response, true);

    return $data['inscricoes'] ?? [];
}
}
