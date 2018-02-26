package nexus;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.utils.Config;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SSSign extends PluginBase implements Listener{
	final String dispatch="§2§k!§r§2[ §6명령어 실행 §2]§k!";
	Config config;
	@Override
	public void onEnable(){
		this.getDataFolder().mkdirs();
		this.config=new Config(this.getDataFolder()+"/config.yml",Config.YAML);
		this.getServer().getPluginManager().registerEvents(this,this);
	}
	@Override
	public void onDisable(){
		this.config.save();
	}
	@EventHandler
	public void onTouch(PlayerInteractEvent ev){
		Player player=ev.getPlayer();
		Block block=ev.getBlock();
		BlockEntity blockEntity=player.getLevel().getBlockEntity(block.getLocation());
		if(!(blockEntity instanceof BlockEntitySign)){
			return;
		}
		BlockEntitySign sign=(BlockEntitySign)player.getLevel().getBlockEntity(block.getLocation());
		String line1=sign.getText()[0];
		String line2=sign.getText()[1];
		String line3=sign.getText()[2];
		if(line1.equals(dispatch)){
			this.getServer().dispatchCommand((CommandSender)player,line3);
		}
	}
	@EventHandler
	public void onSign(SignChangeEvent ev){
		Player player=ev.getPlayer();
		String line1=ev.getLine(0);
		Date date=new Date();
		String name=player.getName();
		String level=player.getLevel().getFolderName();
		String message="["+name+"-"+level+"-"+ev.getBlock().x+"-"+ev.getBlock().y+"-"+ev.getBlock().z+"]";
		for(String line:ev.getLines()){
			message=message+line+" ";
		}
		SimpleDateFormat sdf=new SimpleDateFormat("MM월dd일 HH시mm분ss초");
		config.set(sdf.format(date),message);
		if(line1.equals("dispatch")){
			if(!player.isOp()){
				return;
			}
			ev.setLine(0,dispatch);
		}
	}
}