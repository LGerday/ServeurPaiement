
(function() {
    console.log("Ceci est une fonction auto-exécutante !");
    miseAJourTable();
})();
document.getElementById('updateButton').addEventListener('click', function(e) {
    var id = document.getElementById('productId').value;
    var stock = document.getElementById('productStock').value;
    var price = document.getElementById('productPrice').value;
    var xhr = new XMLHttpRequest();
    var url = "http://127.0.0.1:8081/api/article?id=" + id + "&stock=" + stock + "&prix=" + price;
    xhr.open("POST", url, true);
    xhr.responseType = "text";
    xhr.send();
    miseAJourTable();
    console.log(e);
});

function videTable()
{
    var maTable = document.getElementById("articleTable");
    while (maTable.rows.length > 1) {
        maTable.deleteRow(-1);
    }
}
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

    maTable.querySelector("tbody").appendChild(nouvelleLigne);
}
function miseAJourTable() {
    var xhr = new XMLHttpRequest();

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

    xhr.open("GET", "http://127.0.0.1:8081/api/article", true);
    xhr.responseType = "json";
    xhr.send();
    console.log("2eme fois ready state : "+xhr.readyState + " status :  "+xhr.status);
}

