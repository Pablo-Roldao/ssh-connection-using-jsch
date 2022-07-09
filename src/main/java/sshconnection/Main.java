package sshconnection;

import java.io.ByteArrayOutputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Main {

	public static void main(String[] args) throws JSchException, InterruptedException {
		
		Session sessao = null;
		ChannelExec canal = null;
		ChannelExec canal2 = null;
		
		try {
			JSch jsch = new JSch();
			//informaçoes sobre o servidor ssh
			String usuario = "nome do usuario";
			String host = "ip do host";
			int porta = 22;
			String senha = "senha do usuario";
			String comando = "ls /";
			String comando2 = "ip address";

			//criando sessao com o servidor
			sessao = jsch.getSession(usuario, host, porta);
			sessao.setPassword(senha);
			sessao.setConfig("StrictHostKeyChecking", "no");
			sessao.connect();
			
			//criando canal para executar comando no servidor
			canal = (ChannelExec)sessao.openChannel("exec");
			canal2 = (ChannelExec)sessao.openChannel("exec");
			
			//setando o comando que sera executado
			canal.setCommand(comando);
			canal2.setCommand(comando2);
			
			//criando e setando a variavel para guardar a resposta do mando
			ByteArrayOutputStream respostaStream = new ByteArrayOutputStream();
			canal.setOutputStream(respostaStream);
			ByteArrayOutputStream respostaStream2 = new ByteArrayOutputStream();
			canal2.setOutputStream(respostaStream2);
			
			//conectando canal
			canal.connect();
			canal2.connect();
			
			//so passar daqui quando o canal estiver desconectado (comando tiver sido executado)
			while (canal.isConnected() && canal2.isConnected()) {
				Thread.sleep(100);
			}
			
			//transformando a resposta do comando em String
			String resposta = new String(respostaStream.toByteArray());
			String resposta2 = new String(respostaStream2.toByteArray());
			
			System.out.println("RESPOSTA DO PRIMEIRO COMANDO:\n" + resposta);
			System.out.println();
			System.out.println("RESPOSTA DO SEGUNDO COMANDO:\n" + resposta2);
		} finally {
			if (sessao != null) {
				sessao.disconnect();
			}
			if (canal != null) {
				canal.disconnect();
				canal2.disconnect();
			}
		}
		
	}
	
}
