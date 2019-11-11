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
		$stmt = $this->con->prepare("SELECT id, nome, email, esporte FROM users WHERE email LIKE '$email'");
		$stmt->execute();
		$stmt->bind_result($id, $nome, $email, $esporte);

		$datas = array();

		while($stmt->fetch()){
			$data = array();
			$data['id'] = $id;			
			$data['nome'] = $nome;
			$data['email'] = $email;
			$data['esporte'] = $esporte;
			array_push($datas, $data);	
		}

		return $datas; 
	}
	
	function alteruser($id){
		$stmt = $this->con->prepare("SELECT id, nome, peso, email, esporte FROM users WHERE id LIKE '$id'");
		$stmt->execute();
		$stmt->bind_result($id, $nome, $peso, $email, $esporte);

		$dats = array();

		while($stmt->fetch()){
			$dat = array();
			$dat['id'] = $id;			
			$dat['nome'] = $nome;
			$dat['peso'] = $peso;
			$dat['email'] = $email;
			$dat['esporte'] = $esporte;
			array_push($dats, $dat);	
		}

		return $dats; 
	}
	
	function updateNome($id, $nome){
		$stmt = $this->con->prepare("UPDATE users SET nome = ? WHERE id LIKE ?");
		$stmt->bind_param("si", $nome, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	function updatePeso($id, $peso){
		$stmt = $this->con->prepare("UPDATE users SET peso = ? WHERE id LIKE ?");
		$stmt->bind_param("si", $peso, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	function updateEmail($email, $id){
		$stmt = $this->con->prepare("UPDATE users SET email = ? WHERE id LIKE ?");
		$stmt->bind_param("si", $email, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	function updateSenha($id, $senha){
		$stmt = $this->con->prepare("UPDATE users SET senha = ? WHERE id LIKE ?");
		$stmt->bind_param("si", $senha, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	function updateEsporte($id, $esporte){
		$stmt = $this->con->prepare("UPDATE users SET esporte = ? WHERE id LIKE ?");
		$stmt->bind_param("si", $esporte, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	function createTreino($tempo, $data, $titulo, $forca, $velocity, $fk_id_user){
		$stmt = $this->con->prepare("INSERT INTO treino (tempo, data, titulo, forca, velocity, fk_id_user) VALUES (?, ?, ?, ?, ?, ?)");
		$stmt->bind_param("sssssi", $tempo, $data, $titulo, $forca, $velocity, $fk_id_user);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	function loadingTreiner($fk_id_user){
		$stmt = $this->con->prepare("SELECT id_treino, tempo, data, titulo FROM treino WHERE fk_id_user LIKE '$fk_id_user'");
		$stmt->execute();
		$stmt->bind_result($id_treino, $tempo, $data, $titulo);

		$treinos = array();

		while($stmt->fetch()){
			$treino = array();
			$treino['id_treino'] = $id_treino;			
			$treino['tempo'] = $tempo;
			$treino['data'] = $data;
			$treino['titulo'] = $titulo;
			array_push($treinos, $treino);	
		}

		return $treinos; 
	}

    function deleteTreiner($id_treino){
		$stmt = $this->con->prepare("DELETE FROM treino WHERE id_treino = ? ");
		$stmt->bind_param("i", $id_treino);
		if($stmt->execute())
			return true; 
		return false; 
	}
}