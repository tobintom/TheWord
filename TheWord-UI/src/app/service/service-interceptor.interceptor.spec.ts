import { TestBed } from '@angular/core/testing';

import { ServiceInterceptorInterceptor } from './service-interceptor.interceptor';

describe('ServiceInterceptorInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      ServiceInterceptorInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: ServiceInterceptorInterceptor = TestBed.inject(ServiceInterceptorInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
