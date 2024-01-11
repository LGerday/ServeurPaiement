
(function() {
    console.log("Ceci est une fonction auto-exécutante !");
    <!-- Initialisation de la table des produits au démarrage  -->
    miseAJourTable();
})();

<!-- Listener bouton Mettre a Jour -->
document.getElementById('updateButton').addEventListener('click', function(e) {
    var id = document.getElementById('productId').value;
    var stock = document.getElementById('productStock').value;
    var price = document.getElementById('productPrice').value;
    <!--  Recuperation des valeurs et test dessus pour s'assurer que les valeurs ne sont pas absurdes-->
    if(!isNaN(price) && !isNaN(stock) && Number.isInteger(parseFloat(stock))){
        console.log("test valeur true");
        <!-- Création de la requete vers notre API -->
        <!-- Ici POST pour modifier les données de la bd -->
        var xhr = new XMLHttpRequest();
        var url = "http://127.0.0.1:8081/api/article?id=" + id + "&stock=" + stock + "&prix=" + price;
        xhr.open("POST", url, true);
        xhr.responseType = "text";
        xhr.send();
        <!-- Mise a jour de la table avec les nouvelles données -->
        miseAJourTable();
    }
    else
        alert("Input incorrect");

});

function videTable()
{
    var maTable = document.getElementById("articleTable");
    while (maTable.rows.length > 1) {
        maTable.deleteRow(-1);
    }
}
<!-- Variable pour retenir la ligne selectionner -->
let oldrow;
<!-- Fonction pour créer le tableau d'article dynamiquement avec un listener pour le selectioner d'un element -->
function ajouteLigne(id, intitule, stock, prix) {
    var maTable = document.getElementById("articleTable");
    var nouvelleLigne = document.createElement("tr");

    var celluleId = document.createElement("td");
    celluleId.textContent = id;

    var celluleIntitule = document.createElement("td");
    celluleIntitule.textContent = intitule;

    var cellulePrix = document.createElement("td");
    cellulePrix.textContent = prix;

    var celluleStock = document.createElement("td");
    celluleStock.textContent = stock;

    nouvelleLigne.appendChild(celluleId);
    nouvelleLigne.appendChild(celluleIntitule);
    nouvelleLigne.appendChild(cellulePrix);
    nouvelleLigne.appendChild(celluleStock);
    <!-- Listener pour la selection de ligne -->
    maTable.querySelector("tbody").appendChild(nouvelleLigne);
    nouvelleLigne.addEventListener('click', function() {
        <!-- Quand ligne selectionner on met en surbrillance -->
        <!-- Si old row existe, on lui retire la surbrillance pour l'ajouter a la nouvelle ligne selectionner -->
        if(oldrow)
            oldrow.classList.remove('highlighted')


        nouvelleLigne.classList.add('highlighted');
        oldrow = nouvelleLigne;
        <!-- Affichage de la ligne selectionner dans le tableau de modification -->
        selectRow(id,intitule,prix,stock);
    });
}

function selectRow(id,intitule,prix,stock){
    const productIdInput = document.getElementById('productId');
    const productNameInput = document.getElementById('productName');
    const productPriceInput = document.getElementById('productPrice');
    const productStockInput = document.getElementById('productStock');
    const productImage = document.getElementById('productImage');
    if(intitule === "pommes de terre")
        productImage.src = '/images/pommesdeterre.jpg';
    else
        productImage.src = '/images/'+intitule+'.jpg';
    console.log(productImage.src);
    productIdInput.value = id;
    productNameInput.value = intitule;
    productPriceInput.value = prix;
    productStockInput.value = stock;

}
function miseAJourTable() {
    var xhr = new XMLHttpRequest();

    <!--  Listener pour mettre a jour la table lorsque la requete GET a fini et renvoie le code 200 -->
    xhr.addEventListener('readystatechange', function() {
        console.log("ready state : "+xhr.readyState + " status :  "+xhr.status);
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(this.response);
            var articles = this.response;
            console.log(articles);
            videTable();
            articles.forEach(function(article) {
                ajouteLigne(article.Id, article.Nom, article.Stock, article.Prix);
            });
        } else if (xhr.readyState === 4) {
            alert("Une erreur est survenue...");
        }
    });
    <!-- Création de la requete GET pour récupérer les articles de la BD -->
    xhr.open("GET", "http://127.0.0.1:8081/api/article", true);
    xhr.responseType = "json";
    xhr.send();
    console.log("2eme fois ready state : "+xhr.readyState + " status :  "+xhr.status);
}

