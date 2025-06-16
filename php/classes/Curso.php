<?php
require_once "ApiService.php";

class Curso extends ApiServices
{
    public function getCursos()
    {
        return $this->request('/cursos');
    }
}
