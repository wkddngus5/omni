
function getSelectItemValue(selectItem){
	if(selectItem == "[]"){
		selectItem = selectItem.replace("[]","");
	}else{
		selectItem = selectItem.replace("[","'").replace(/,/g,"','").replace("]","',").replace(/ /g,"");
	}
	//alert(selectItem);
	return selectItem;
}