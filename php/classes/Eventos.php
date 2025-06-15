<?php
require_once "ApiService.php";

class Eventos extends ApiServices
{
    public function getEventos()
    {
        return $this->request('/eventos');
    }
}
