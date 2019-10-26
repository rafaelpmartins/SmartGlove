<?php 
	//Recebendo a classe dboperation
	require_once '../includes/DbOperation.php';
	
	// Método que irá validar todos os parâmetros que estão disponíveis
	// vamos passar os parâmetros necessários para este método
	function isTheseParametersAvailable($params){
		//assumindo que todos os parâmetros estão disponíveis
		$available = true; 
		$missingparams = ""; 
		
		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}
		
		//se os parâmetros estiverem faltando
		/*if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
			
			//exibindo os erros
			echo json_encode($response);
			
			//parando a execução adicional
			die();
		}*/
	}
	
	//matriz para exibir a resposta
	$response = array();
	
	
	// se for uma chamada de API
	// isso significa que um parâmetro get chamado api call é definido na URL
	// e com este parâmetro estamos concluindo que é uma chamada de API
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'createuser':
				//primeiro verifique os parâmetros necessários para este pedido estão disponíveis ou não
				isTheseParametersAvailable(array('nome', 'peso', 'email', 'senha', 'esporte'));
				
				//criando um novo objeto dboperation
				$db = new DbOperation();
				
				//criando um novo registro no banco de dados
				$result = $db->createUser(
					$_POST['nome'],
					$_POST['peso'],
					$_POST['email'],
					$_POST['senha'],
					$_POST['esporte']
				);
				
				// se o registro for criado com sucesso
				if($result){
					//registro é criado significa que não há erro
					$response['error'] = false; 
					
					//na mensagem temos uma mensagem de sucesso
					$response['message'] = 'cadastro realizado com sucesso';
				}else{
					// se o registro for não, significa que há um erro
					$response['error'] = true; 
					// e nós temos a mensagem de erro
					$response['message'] = 'Email já cadastrado, tente outro';
				}
			break;

			case 'loginuser':
				isTheseParametersAvailable(array('email', 'senha'));
				$db = new DbOperation();
				
				//criando um novo registro no banco de dados
				$result = $db->loginUser($_POST['email']);

				if($result){
					//registro é criado significa que não há erro
					$response['error'] = false; 
					
					//na mensagem temos uma mensagem de sucesso
					if($_POST['senha'] == $result){
						$response['message'] = 'logado';
					}
					else{
						$response['message'] = 'senha incorreta';
					}
				}
				else{
					// se o registro for não, significa que há um erro
					$response['error'] = "true"; 
					// e nós temos a mensagem de erro
					$response['message'] = "Usuário não existente";
				}
			break;

			case 'loadinguser':
				isTheseParametersAvailable(array('email'));
				$db = new DbOperation();
				
				$response['error'] = false;
				$response['datas'] = $db->loadingUser($_POST['email']);
			break;
			
			case 'alteruser':
				isTheseParametersAvailable(array('id'));
				$db = new DbOperation();
				
				$response['error'] = false;
				$response['message'] = 'Clique e segure para alterar';
				$response['dats'] = $db->alteruser($_POST['id']);
			break;
			
			case 'updatenome':
				isTheseParametersAvailable(array('id', 'nome'));
				$db = new DbOperation();
				
				$result = $db->updateNome($_POST['id'], $_POST['nome']);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Nome alterado com sucesso';
				}else{
					$response['error'] = true; 
					$response['message'] = 'Algum erro ocorreu. Por favor tente novamente';
				}
			break; 
			
			case 'updatepeso':
				isTheseParametersAvailable(array('id', 'peso'));
				$db = new DbOperation();
				
				$result = $db->updatePeso($_POST['id'], $_POST['peso']);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Peso alterado com sucesso';
				}else{
					$response['error'] = true; 
					$response['message'] = 'Algum erro ocorreu. Por favor tente novamente';
				}
			break;
			
			case 'updateemail':
				isTheseParametersAvailable(array('email', 'id'));
				$db = new DbOperation();
				
				$result = $db->updateEmail($_POST['email'], $_POST['id']);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Email alterado com sucesso';
				}else{
					$response['error'] = true; 
					$response['message'] = 'Algum erro ocorreu. Por favor tente novamente';
				}
			break;
			
			case 'updatesenha':
				isTheseParametersAvailable(array('id', 'senha'));
				$db = new DbOperation();
				
				$result = $db->updateSenha($_POST['id'], $_POST['senha']);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Senha alterada com sucesso';
				}else{
					$response['error'] = true; 
					$response['message'] = 'Algum erro ocorreu. Por favor tente novamente';
				}
			break;
			
			case 'updateesporte':
				isTheseParametersAvailable(array('id', 'esporte'));
				$db = new DbOperation();
				
				$result = $db->updateEsporte($_POST['id'], $_POST['esporte']);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Esporte alterado com sucesso';
				}else{
					$response['error'] = true; 
					$response['message'] = 'Algum erro ocorreu. Por favor tente novamente';
				}
			break;

		}
		
	}else{
		//se não for uma chamada api
		//respondendo com os valores apropriados para array
		$response['error'] = true; 
		$response['message'] = 'Chamda de API inválida.';
	}
	
	//exibindo a resposta na estrutura do JSON
	echo json_encode($response);