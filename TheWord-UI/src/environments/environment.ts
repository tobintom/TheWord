// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  AUTH_URL:'http://192.168.99.100:5000/theword/',
  OAUTH_CLIENT:'theWord-client',
  OAUTH_SECRET:'secret',
  USER_NAME:'56ae9949-fd44-4b8f-a92e-d70301063902',
  PASSWORD:'dRx2FZEEPWQ3JWvJceJf8Bvm9vWqxXhA',
  WORD_API_URL:'http://192.168.99.100:5000/theword/v1/bible/',
  META_API_URL:'http://192.168.99.100:5000/theword/v1/bibles',
  SWAGGER_URL: 'http://192.168.99.100:5000/swagger-ui.html'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
