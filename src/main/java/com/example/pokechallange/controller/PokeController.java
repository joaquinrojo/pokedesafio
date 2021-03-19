package com.example.pokechallange.controller;

import com.example.pokechallange.http.WebClient;
import com.google.gson.*;
import org.apache.http.client.ClientProtocolException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping({"/externos"})

public class PokeController {

    @GetMapping(path = {"/pokemon/{name}"})
    public ResponseEntity findByName(@PathVariable String name) throws Exception {
        WebClient client = new WebClient();
        Gson gson = new Gson();
        String json="";
        try {
             json = (client.getContentFromUrl("https://pokeapi.co/api/v2/pokemon/" + name));

        }catch (ClientProtocolException e){
            return new ResponseEntity<>(
                    "{\"error\":\"pokemon no encontrado\"}", HttpStatus.NOT_FOUND);
        }
        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jsonPoke = new JsonObject();
        jsonPoke.add("weight", jelement.getAsJsonObject().get("weight"));
        jsonPoke.add("tipos", (jelement.getAsJsonObject().get("types").getAsJsonArray()));
        jsonPoke.add("habilidades", jelement.getAsJsonObject().get("moves").getAsJsonArray());


        //Busco la foto
        jsonPoke.add("image", jelement.getAsJsonObject().get("sprites").getAsJsonObject().get("back_default"));

        //evoluciones
        String urlSpecies = jelement.getAsJsonObject().get("species").getAsJsonObject().get("url").getAsString();
        String jsonSpecies = (client.getContentFromUrl(urlSpecies));
        JsonElement jelementSpecies = new JsonParser().parse(jsonSpecies);
        String urlEvolutionChain=jelementSpecies.getAsJsonObject().get("evolution_chain").getAsJsonObject().get("url").getAsString();


        String jsonEvolutionChain = (client.getContentFromUrl(urlEvolutionChain));
        System.out.println(jsonEvolutionChain);
        JsonElement jelementEvolutionChain = new JsonParser().parse(jsonEvolutionChain);

        JsonElement evoluciones=jelementEvolutionChain.getAsJsonObject().get("chain");

        JsonArray arregloEvoluciones=null;

        JsonArray arrayEvoluciones=new JsonArray();

        arrayEvoluciones.add(evoluciones.getAsJsonObject().get("species").getAsJsonObject().get("name"));


        if(evoluciones.getAsJsonObject().get("evolves_to")!=null){
             arregloEvoluciones=evoluciones.getAsJsonObject().get("evolves_to").getAsJsonArray();
        }

        while(arregloEvoluciones!=null && arregloEvoluciones.size()>0){
            //solo obtengo 1 evolucion por pokemon
            arrayEvoluciones.add(arregloEvoluciones.get(0).getAsJsonObject().get("species").getAsJsonObject().get("name"));
         //   System.out.println(arregloEvoluciones.get(0).getAsJsonObject().get("species").getAsJsonObject().get("name").getAsString());
            arregloEvoluciones=arregloEvoluciones.get(0).getAsJsonObject().get("evolves_to").getAsJsonArray();
        }


        jsonPoke.add("evoluciones", (arrayEvoluciones));

        String output = gson.toJson(jsonPoke);
        return new ResponseEntity<>(
                output, HttpStatus.OK);
    }



}
