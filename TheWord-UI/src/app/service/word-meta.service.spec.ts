import { TestBed } from '@angular/core/testing';

import { WordMetaService } from './word-meta.service';

describe('WordMetaService', () => {
  let service: WordMetaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WordMetaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
