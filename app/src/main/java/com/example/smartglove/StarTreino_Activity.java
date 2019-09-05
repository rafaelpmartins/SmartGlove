package com.example.smartglove;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class StarTreino_Activity extends AppCompatActivity {

    private TextView txtTitulo, txtDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_treino_layout);

        txtTitulo = (TextView) findViewById(R.id.txtIdTitulo);
        txtDescricao = (TextView) findViewById(R.id.txtIdDescricao);

        Intent intent = getIntent();
        String titulo = intent.getExtras().getString("Titulo");

        txtTitulo.setText(titulo);

        if (titulo.equals("Aikido")){
            txtDescricao.setText("A maioria dos exercícios técnicos do aikido consistem em projeções e/ou arremessos, onde a aprendizagem de métodos de queda são essenciais ao praticante. As técnicas específicas de ataque incluem tanto golpes de punho como em garra, já as técnicas de defesa consistem em projecções, desvios e torções/contusões das articulações do adversário.");
        }
        if (titulo.equals("Boxe")){
            txtDescricao.setText("Sparring é uma simulação de luta entre pugilistas. Muitos lutadores profissionais começam como sparring, antes de se profissionalizarem. Alguns exemplos de sparrings, que depois vieram a se tornar campeões incluem: Larry Holmes, sparring de Muhammad Ali; Oscar de la Hoya, sparring de Julio Cesar Chávez; e Riddick Bowe, sparring de Evander Holyfield.");
        }
        if (titulo.equals("Caratê")){
            txtDescricao.setText("O treinamento tradicional de caratê deve começar e terminar com um breve momento de meditação, mokuso, cuja finalidade é preparar o carateca para os ensinamentos que receberá e, depois, refletir sobre os mesmos. A cada momento ou exercício faz-se saudação no começo e no fim, sendo costume difundido em vários dojôs fazer uma reverência ao entrar e sair do sítio.");
        }
        if (titulo.equals("Jeet Kune Do")){
            txtDescricao.setText("Sem formas fixas, o Jeet Kune Do é um sistema criado por Bruce Lee, que mais que uma arte marcial, é uma síntese de seus próprios pensamentos. Em última instância o Jeet Kune Do (Caminho do golpe que intercepta) não se resume a uma técnica, mas essencialmente a uma filosofia advinda principalmente da corrente Taoísta, com influências do Zen Budismo, e de Krishnamurti.");
        }
        if (titulo.equals("Jiu Jitsu")){
            txtDescricao.setText("Atualmente, ainda se pratica o jujutsu' associado aos samurais do antigo Japão. Note-se que, no caso dessa arte tradicional, as palavras ju (flexibilidade, gentil, suave) e jutsu (arte, técnica) são as mesmas do jiu-jítsu mais utilizado para classificar o chamado jiu-jitsu brasileiro, criado pelos irmãos Gracie. Crê-se que essa vertente tenha sido propagada na Europa por Minoru Mochizuki.");
        }
        if (titulo.equals("Kick Boxing")){
            txtDescricao.setText("O kickboxing ou full contact em sentido restrito é muitas vezes confundido com o muay thai (boxe tailandês). Ambos são semelhantes mas com extremas diferenças, não apenas nas regras, mas também na prática. Contudo, o foco do lutador durante a luta é diferente. Nas regras do muay thai, tem os golpes de cotovelos a mais. Já os golpes de joelhos são permitidos tanto no muay thai como na modalidade K-1 do Kickboxing.");
        }
        if (titulo.equals("Muay Thai")){
            txtDescricao.setText("Hoje em dia o muay thai ter-se-á convertido num símbolo nacional do reino da Tailândia, sendo o desporto mais praticado no país. As suas raízes encontram-se no muay boran, uma arte ancestral que foi desenvolvida a partir de uma forma de luta designada de chupasart.");
        }
        if (titulo.equals("Wing Chun")){
            txtDescricao.setText("O Wing Chun (Ving Tsun ou Wing Tsun) é um sistema de luta surgido no sul da China que se distingue pela economia de movimentos sendo um sistema de defesa pessoal. Simples e eficiente, descarta todo movimento acrobático. É uma arte marcial singular, desenvolvida para permitir que qualquer tipo de pessoa, independentemente de tamanho, força ou sexo, possa se defender de agressores maiores e mais fortes.");
        }
    }
}
