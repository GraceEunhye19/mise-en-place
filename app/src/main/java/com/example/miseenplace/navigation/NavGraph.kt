package com.example.miseenplace.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.miseenplace.features.converter.ConverterScreen
import com.example.miseenplace.features.notes.NotesScreen
import com.example.miseenplace.features.timer.TimerScreen

@Composable
fun NavGraph(
    navController: NavController
){
    NavHost(
        navController = navController as NavHostController,
        startDestination = BottomNav.Converter.route
    ){
        composable(BottomNav.Converter.route){ConverterScreen()}
        composable(BottomNav.Notes.route){NotesScreen()}
        composable(BottomNav.Timer.route){TimerScreen()}
    }
}