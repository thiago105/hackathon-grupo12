<?php

class ApiService
{
    private string $baseUrl;

    public function __construct()
    {
        $this->baseUrl = 'http://localhost:3306/';
    }

    public function getUsers()
    {
        $url = $this->baseUrl . '';

        $curl = curl_init($url);
        curl_setopt_array($curl, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_TIMEOUT => 10,
            CURLOPT_HTTPHEADER => [
                'Accept: application/json',
                'User-Agent": eventos-api'
            ]
        ]);

        $response = curl_exec($curl);
        curl_close($curl);

        if (!$response){
            return[];
        }

        return json_decode($response, true);
    }
}