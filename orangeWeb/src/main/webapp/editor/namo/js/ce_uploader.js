var pe_acg={Upload:function(form,pe_bnd,pe_abd,pe_bmO,pe_byP,pe_btp){ce$(form).ajaxForm({formData:pe_btp,url:pe_bnd,beforeSend:function(e){pe_abd();},complete:function(xhr){pe_bmO(xhr);},pe_bDb:function(event,position,pe_WL,pe_bwj){var pe_bKt=pe_bwj+'\x25';},error:function(pe_uA,status,request){pe_byP();}}).submit();}};