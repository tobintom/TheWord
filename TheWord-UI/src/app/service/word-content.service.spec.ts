import { TestBed } from '@angular/core/testing';

import { WordContentService } from './word-content.service';

describe('WordContentService', () => {
  let service: WordContentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WordContentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
