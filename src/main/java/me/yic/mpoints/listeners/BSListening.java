package me.yic.mpoints.listeners;

import me.yic.mpoints.depend.Bossshop;
import org.black_ixx.bossshop.events.BSRegisterTypesEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class BSListening implements Listener{

	  @EventHandler
	  public void onRegister(BSRegisterTypesEvent event)  {
	    new Bossshop().register();
	  }
	  
}
