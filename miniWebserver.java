import java.net.*;
import java.io.*;

public class miniWebserver { 
	public static void main(String args[]) throws IOException { 
		ServerSocket server = new ServerSocket(8080); 
		System.out.println("Listening for connection on port 8080 ...."); 

		while (true) { 
			try (Socket socket = server.accept()) {
				InputStreamReader isr = new InputStreamReader(socket.getInputStream()); 
				BufferedReader reader = new BufferedReader(isr);
				String line = reader.readLine(); 
				String links = "<link rel='stylesheet' href='https://storage.googleapis.com/code.getmdl.io/1.0.6/material.indigo-pink.min.css'> \n<script src='https://storage.googleapis.com/code.getmdl.io/1.0.6/material.min.js'></script> \n<link rel='stylesheet' href='https://fonts.googleapis.com/icon?family=Material+Icons'>\n<link rel='stylesheet' href='http://fonts.googleapis.com/css?family=Roboto:300,400,500,700' type='text/css'>";
				String style = "<style>html {text-align: center;}body { width:70%;margin-left:auto;margin-right:auto;}</style>";

				PrintWriter writer = new PrintWriter("index.html", "UTF-8");
				String httpResponse = "HTTP/1.0 200 OK \nContent-Type: text/html\n Server: Bot \n\n<html>\n<head>\n" + links + style +"\n</head>\n<body>";
				if(line.startsWith("GET") || line.startsWith("POST") ){
					String table = "<table class='mdl-data-table mdl-js-data-table'>\n <thead> \n<tr> \n<th>Name</th>\n <th>Value</th>\n </tr>\n </thead> <tbody>\n ";
					httpResponse = httpResponse + table;
					
					while (!line.isEmpty() ) {
						if(line.startsWith("GET") || line.startsWith("POST")){
							httpResponse = httpResponse + " <tr><td>HTTP Header</td><td>"+ line +"</td></tr>\n";  
						}else{
							int index = line.indexOf(':');
							String name = line.substring(0,index);
							String value = line.substring(index+1);
							httpResponse = httpResponse + " <tr><td>"+name+"</td><td>"+ value +"</td></tr>\n";  
						}												
						
						System.out.println(line); 
						line = reader.readLine();
					}
					httpResponse = httpResponse + "</tbody>\n</table>";
					httpResponse = httpResponse + "</body>\n</html>";
								
				}else{
					httpResponse =  httpResponse + "HTTP/1.1 500 Server Error\r\n";	//http header
					httpResponse = httpResponse + "Content-Type: text/html\r\n\n";	//Content-Type					
					httpResponse = httpResponse + "</body>\n</html>";
				}	
				
				writer.println(httpResponse);
				writer.close(); 
					
				socket.getOutputStream().write(httpResponse.getBytes("UTF-8")); 
				
			}catch(SocketTimeoutException s){
			    System.out.println("Socket timed out!");
			    break;
			 }catch(IOException e){
			    e.printStackTrace();
			    break;
			 } 
		} 
	} 
}

