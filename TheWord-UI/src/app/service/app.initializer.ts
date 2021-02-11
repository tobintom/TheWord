import { AuthService } from "./auth.service";
import { JwtHelperService } from "@auth0/angular-jwt";
import { AppHelper } from "./app.helper";

const helper = new JwtHelperService();

export function appInitializer(auth: AuthService, appHelper: AppHelper) {    
    console.warn('Initialize');
        return () => new Promise(resolve => {
            // initialize
            
            if(helper.isTokenExpired(auth.getToken())){                
                auth.authenticate()
                    .subscribe()
                    .add(resolve);
            }else{
                resolve(true);
            }            
        });    
        
}