import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'modelId'
})
export class ModelIdPipe implements PipeTransform {

  transform(value: string): string {
    return value.substr(0, value.indexOf('-'));
  }

}
