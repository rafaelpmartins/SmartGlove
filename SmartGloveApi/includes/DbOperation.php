<?php
 
class DbOperation
{
    //Link de conexão com banco de dados
    private $con;
 
    //Construtor da classe
    function __construct()
    {
        // Obtendo o arquivo DbConnect.php
        require_once dirname(__FILE__) . '/DbConnect.php';
 
        // Criando um objeto DbConnect para se conectar ao banco de dados
        $db = new DbConnect();
 
        
		// Inicializando o link de conexão
        // chamando o método connect da classe DbConnect
        $this->con = $db->connect();
    }
	
	/*
	* Operação de criação
	* Quando esse método é chamado, um novo registro é criado no banco de dados
	*/
	function createUser($nome, $data_nasc, $sexo, $email, $senha, $esporte){
		$stmt = $this->con->prepare("INSERT INTO users (nome, data_nasc, sexo, email, senha, esporte) VALUES (?, ?, ?, ?, ?, ?)");
		$stmt->bind_param("ssssss", $nome, $data_nasc, $sexo, $email, $senha, $esporte);
		if($stmt->execute())
			return true; 
		return false; 
	}

}