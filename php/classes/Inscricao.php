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
    
    $context = stream_context_create([
        'http' => [
            'method' => 'GET',
            'header' => "Content-type: application/json\r\n"
        ]
    ]);

    $response = file_get_contents($url, false, $context);

    if ($response === false) {
        return [];
    }

    $data = json_decode($response, true);

    if (isset($data['inscricoes']) && is_array($data['inscricoes'])) {
        foreach ($data['inscricoes'] as &$inscricao) {
            if (!isset($inscricao['usuario_id'])) {
                $inscricao['usuario_id'] = $usuario_id;
            }
        }
    }

    return $data['inscricoes'] ?? [];
}
}
