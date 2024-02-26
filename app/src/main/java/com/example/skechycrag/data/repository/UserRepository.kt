package com.example.skechycrag.data.repository

import com.example.skechycrag.data.model.route.RouteProvider
import com.example.skechycrag.data.model.user.*
import com.example.skechycrag.data.network.UserServices
import com.example.skechycrag.ui.constants.Constants.Companion.USERNAME
import javax.inject.Inject

//Esta es la clase que vamos a llamar desde DOMAIN, desde el USERCASE, para recoger las citas y la que decide de donde las saca
class UserRepository @Inject constructor(
    private val userServices : UserServices,
    private val userProvider: UserProvider,
    private val routeProvider: RouteProvider
){

    suspend fun getAllUsers(username: String):UserModel?{
        val user = userServices.getUsers(username)
        userProvider.user = user
        return user
    }

    suspend fun getLogBook(username: String): List<UserRouteModel> {
        if(username == userProvider.user?.username ){
            return userServices.getLogBook(userProvider.user?.user_id?: return emptyList())
        }
        return emptyList()
    }


    suspend fun getUserLevel(): UserModel? {
        return userProvider.user
    }

    suspend fun getMoreInfoRoute(routeName: String): List<MoreInfoRouteModel> {
        var routeId = ""
        for(route in routeProvider.routeList){
            if(routeName == route.route_name){
                routeId = route.route_id
            }
        }
        var matchFound = false // Flag to track if a matching ID is found

        if (routeProvider.routeIdProvide.size > 0) {
            for (provideId in routeProvider.routeIdProvide) {
                if (routeId != "") {
                    if (provideId == routeId) {
                        var index = routeProvider.routeIdProvide.indexOf(routeId)
                        matchFound = true // Match found, set the flag to true
                        return routeProvider.climberList[index]
                    }
                }
            }
            // After the loop, check if a match was found
            if (!matchFound) {
                // If no match was found, execute the logic that was previously in the else block
                routeProvider.routeIdProvide.add(routeId)
                var infoList = userServices.getMoreInfoRoute(routeId)
                routeProvider.climberList.add(infoList)
                return infoList
            }
        } else {
            // This block now only executes if the routeIdProvide list is initially empty
            routeProvider.routeIdProvide.add(routeId)
            var infoList = userServices.getMoreInfoRoute(routeId)
            routeProvider.climberList.add(infoList)
            return infoList
        }

        return emptyList()
    }

    suspend fun changeUserLevel(user: UserModel?) {
        userProvider.user = user
        userServices.changeUserLevel(user)
    }

}