import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;


import java.util.Scanner;

public class KonsolProgram {
    public static void main(String[] args) throws UnirestException {
        Scanner sc = new Scanner(System.in);
        String login, password;
        System.out.println("Indtast brugernavn: ");
        login = sc.nextLine();
        System.out.println("Indtast password: ");
        password = sc.nextLine();

        JSONObject json = new Login(login, password).toJSON();
        HttpResponse response = Unirest.post("http://www.lmlige.dk/public/login")
                .header("Content-Type", "application/json")
                .body(json).asString();
        String autheader = "Bearer " + response.getBody();

        if(response.getStatus() != 200){
            String exceptionRes = "Status code: " + response.getStatus() +"\n"+ response.getStatusText();
            System.out.println(exceptionRes);
            System.exit(1);
        }

        System.out.println("--------------------\n" +
                "Logged in\n" +
                "--------------------\n");

        System.out.println("--------------------------------\n" +
                "Tryk F for at få de fremtidige maddage\n" +
                "Tryk P for at få de forrige maddage\n"+
                "Tryk S for at få status\n" +
                "Tryk Q for at lukke programmet\n" +
                "--------------------------------\n");

        while (true) {
            String com = sc.nextLine().toLowerCase();
            switch (com) {
                case "f":
                    response = Unirest.get("http://www.lmlige.dk/public/futuredates")
                            .header("Content-Type", "application/json")
                            .header("Authorization", autheader).asString();
                    System.out.println(response.getBody());
                    break;
               case "p":
                    response = Unirest.get("http://www.lmlige.dk/public/pastdates")
                            .header("Content-Type", "application/json")
                            .asString();
                    System.out.println(response.getBody());
                    break;
                case "s":
                    response = Unirest.get("http://www.lmlige.dk/public/status")
                            .header("Content-Type", "application/json")
                            .asString();
                    System.out.println(response.getBody());
                    break;
                case "q":
                    System.exit(1);
                    break;
                default:
                    System.out.println("--------------------------------\n" +
                            "Dette er ikke et ordentligt input, prøv et af disse i stedet:\n"+
                            "Tryk F for at få de fremtidige maddage\n" +
                            "Tryk P for at få de forrige maddage\n"+
                            "Tryk S for at få status\n" +
                            "Tryk Q for at lukke programmet\n" +
                            "--------------------------------\n");
                    break;
            }
        }
    }
}

class Login {

    public Login(String brugernavn, String password) {
        this.brugernavn = brugernavn;
        this.password = password;
    }

    public String brugernavn;
    public String password;


    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        jo.put("login", brugernavn);
        jo.put("password", password);
        return jo;
    }
}
