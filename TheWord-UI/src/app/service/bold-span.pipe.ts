import { Pipe, PipeTransform, Sanitizer, SecurityContext } from '@angular/core';
import { DomSanitizer,SafeHtml  } from '@angular/platform-browser'

@Pipe({
  name: 'boldSpan'
})
export class BoldSpanPipe implements PipeTransform {

  constructor(
    private sanitizer: DomSanitizer
  ) {}

  transform(value: string, regex): SafeHtml  {
    return this.sanitize(this.replace(value, regex));
  }

  replace(str, regex) {
    return str.replace(new RegExp(`(${regex})`, 'gi'), '<span style="font-weight:bold;background-color: #00ffab78;">$1</span>');
  }

  sanitize(str) {
    return this.sanitizer.bypassSecurityTrustHtml(str);
  }
}