package br.com.licenseplate.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.DeletedRequest
import br.com.licenseplate.interactor.StamperInteractor
import java.lang.Exception

class StamperViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = StamperInteractor(app.applicationContext)
    var resultado = MutableLiveData<Set<AuthorizationClient>>()
    var i = 0

    fun authorizationList() {
        val set = mutableSetOf<AuthorizationClient>()
        interactor.authorizationList { result ->
            result.forEach { aut ->
                set.add(aut)
            }
            resultado.value = set
        }
    }

    fun authorizationReceivedList() {
        val set = mutableSetOf<AuthorizationClient>()
        interactor.authorizationReceivedList { result ->
            result.forEach { aut ->
                set.add(aut)
            }
            resultado.value = set
        }
    }

    fun authorizationHistoryList() {
        val set = mutableSetOf<AuthorizationClient>()

        interactor.authorizationHistoryList { result ->
            result.forEach { aut ->
                set.add(aut)
            }
            resultado.value = set
        }
    }

    fun authorizationDeliveredList() {
        val set = mutableSetOf<AuthorizationClient>()
        interactor.authorizationDeliveredList { result ->
            result.forEach { aut ->
                set.add(aut)
            }

            resultado.value = set
            i++
        }
    }

    fun deleteRequest(
        authorization: AuthorizationClient,
        reason: String,
        callback: (String) -> Unit
    ) {
        val deleted = DeletedRequest(authorization.authorization, reason, authorization.id)
        interactor.deleteRequest(authorization, deleted)
        val response =
            "Solicitação da placa '${authorization.authorization?.placa}' excluída com sucesso!"
        callback(response)
    }

    fun receiveRequest(authorization: AuthorizationClient, callback: (String) -> Unit) {
        interactor.receiveRequest(authorization)

        val response =
            "Autorização da placa '${authorization.authorization?.placa}' recebida com sucesso!"
        callback(response)
    }

    fun finishRequest(authorization: AuthorizationClient, callback: (String) -> Unit) {
        interactor.finishRequest(authorization)

        val response =
            "Autorização da placa '${authorization.authorization?.placa}' finalizada com sucesso!"
        callback(response)
    }

    fun deliverRequest(authorization: AuthorizationClient, callback: (String) -> Unit) {
        interactor.deliverRequest(authorization)

        val response =
            "Autorização da placa '${authorization.authorization?.placa}' entregue com sucesso!"
        callback(response)
    }

    fun editInfo(carPrice: String, motoPrice: String, phone: String, callback: (String) -> Unit){
        try {
            val newCarPrice = carPrice.toDouble()
            val newMotoPrice = motoPrice.toDouble()

            interactor.editInfo(newCarPrice, newMotoPrice, phone)

            val resultado = "Informações atualizadas com sucesso!"
            callback(resultado)
        }catch (e: Exception){
            val resultado = "Preços inválidos. Por favor ajuste e tente novamente!"
            callback(resultado)
        }
    }
}