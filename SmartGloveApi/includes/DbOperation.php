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
	function createUser($nome, $peso, $email, $senha, $esporte){
		$stmt = $this->con->prepare("INSERT INTO users (nome, peso, email, senha, esporte) VALUES (?, ?, ?, ?, ?)");
		$stmt->bind_param("sssss", $nome, $peso, $email, $senha, $esporte);
		if($stmt->execute())
			return true; 
		return false; 
	}

	function loginuser($email){
		$stmt = $this->con->prepare("SELECT senha FROM users WHERE email LIKE '$email'");
		$stmt->execute();
		$stmt->bind_result($senha);

		$password = "";

		while($stmt->fetch()){
			$password = $senha;
		}

		return $password; 
	}

	function loadinguser($email){
		$stmt = $this->con->prepare("SELECT nome, email, esporte FROM users WHERE email LIKE '$email'");
		$stmt->execute();
		$stmt->bind_result($nome, $email, $esporte);

		$datas = array();

		while($stmt->fetch()){
			$data = array();	
			$data['nome'] = $nome;
			$data['email'] = $email;
			$data['esporte'] = $esporte;
			array_push($datas, $data);	
		}

		return $datas; 
	}


}