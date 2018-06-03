var dialog = new Dialog();

var showMesage = function(message, type){
      switch(type){
      case 'success':
              dialog.setTitle("Succes!");
              break;
      case 'error':
              dialog.setTitle("Error!");
              break;
      default:
      case 'info':
              dialog.setTitle("Info!");
              break;
      }
      dialog.showMesage(message);
} 