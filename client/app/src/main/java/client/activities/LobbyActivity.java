package client.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.debernardi.archemii.R;

import java.util.ArrayList;

import client.controller.ClientGameHandler;
import shared.abilities.Ability;
import shared.abilities.*;
import shared.entities.Player;
import shared.general.Party;
import shared.messages.LeavePartyMessage;
import shared.messages.PlayerInfoMessage;
import shared.messages.ReadyMessage;
import shared.messages.StartGameMessage;

public class LobbyActivity extends AppCompatActivity {

	private Button btnStartGame;
	private boolean ready;

	private TextView gamePin;
	private TextView txtPlayers;

	private Ability abilities[] = {new Heal(), new Melee(), new Move(), new Range()};
	private String ability_names[] = {"heal", "melee", "move", "range"};
	private String ability_descriptions[] = {"Heal yourself and other players", "melee attack monsters", "idk lol", "range attack"};

	//contains ID of the slots the ith ability is assigned to
	private int assigned_to_slot[] = {0, 0, 0, 0};
	private int ability_slots[] = {R.id.ability1, R.id.ability2, R.id.ability3};
	private int ability_title_ids[] = {R.id.ability_name1, R.id.ability_name2, R.id.ability_name3};
	private int ability_description_ids[] = {R.id.ability_description1, R.id.ability_description2, R.id.ability_description3};

	private ArrayList<Player> players = new ArrayList<>();

	/**
	 * This method also sets this activity available in the client game handler.
	 * @param savedInstanceState
	 * @author Jelmer Firet and Bram Pulles
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);

		gamePin = findViewById(R.id.game_pin);
		txtPlayers = findViewById(R.id.playersTxtView);

		ClientGameHandler.handler.setLobbyActivity(this);

		// Get the latest update party message send by the server, if available.
		if(ClientGameHandler.handler.getParty() != null)
			updateParty(ClientGameHandler.handler.getParty());

		btnStartGame = findViewById(R.id.btnStartGame);
		toggleButton();
	}

	/**
	 * If the client is the master the button will show text to start the game.
	 * If the client is not master the button will show text to get ready.
	 * @author Bram Pulles
	 */
	private void toggleButton(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(btnStartGame != null) {
					setButton(isMaster());
				}
			}
		});
	}

	/**
	 * Set the button to the correct text according to if the client is master or not.
	 * @param isMaster
	 * @author Bram Pulles
	 */
	private void setButton(boolean isMaster){
		if(isMaster){
			btnStartGame.setText(R.string.start_game);
		}else{
			setReadyTextOnButton();
		}
	}

	/**
	 * Set the ready text on the button accordingly.
	 * @author Bram Pulles
	 */
	private void setReadyTextOnButton(){
		// TODO: Change to strings used from R.
		btnStartGame.setText((ready)? "Ready!" : "Not Ready");
	}

	/**
	 * When the ready button is pressed this function will update the ready button.
	 * @author Bram Pulles
	 */
	private void readyPressed(){
		ready = !ready;
		setReadyTextOnButton();
	}

	/**
	 * @return if the client is the master of the party.
	 * @author Bram Pulles
	 */
	private boolean isMaster(){
		return players != null &&
					players.size() > 0 &&
					players.get(0) != null &&
					players.get(0).equals(ClientGameHandler.handler.getPlayer());
	}

	/**
	 * If the player has chosen an ability for all the ability slots, then
	 * start a random game if the client is the master else sent a ready message.
	 * @param v
	 * @author Bram Pulles
	 */
	public void onButtonPressed(View v){
		if(getAbilities().size() == ability_slots.length) {

			// First send a message with the abilities of the player.
			Player player = ClientGameHandler.handler.getPlayer();
			player.setAbilities(getAbilities());
			ClientGameHandler.sendMessage(new PlayerInfoMessage(player));

			if (isMaster()) {
				ClientGameHandler.sendMessage(new StartGameMessage());
			} else {
				readyPressed();
				ClientGameHandler.sendMessage(new ReadyMessage(ready));
			}
		}else{
			new AlertDialog.Builder(this)
				.setTitle("Abilities")
				.setMessage("Please choose all your abilities.")
				.setPositiveButton(android.R.string.ok, null)
				.create().show();
		}
	}

	private ArrayList<Ability> getAbilities(){
		//TODO: BY ROBERT
		// Return an arraylist with the abilities which are chosen by the player.
		return null;
	}

	/**
	 * Set the game pin and the players accordingly on the screen.
	 * Also change the view of the buttons if needed.
	 * @param party
	 * @author Bram Pulles
	 */
	public void updateParty(final Party party){
		players = party.getPlayers();
		toggleButton();

		// This is necessary because we are not invoking this method from the main thread.
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String players = "MASTER: ";
				for(int i = 0; i < party.getPlayers().size(); i++){
					players += party.getPlayers().get(i).getName() + "\n";
				}

				txtPlayers.setText(players);
				gamePin.setText("Game PIN: " + party.getPartyId());
			}
		});
	}

	/**
	 * Show a dialog screen when the back button is pressed.
	 * If the player leaves a leave party message is send to the server.
	 * @author Bram Pulles
	 */
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
			.setTitle("Really Exit?")
			.setMessage("Are you sure you want to exit the party?")
			.setNegativeButton(android.R.string.no, null)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					LobbyActivity.super.onBackPressed();
					ClientGameHandler.handler.sendMessage(new LeavePartyMessage());
				}
			}).create().show();
	}

	/**
	 * Returns an integers [0, 2]: the ability slot to which this ID belongs. Returns -1 if this ID was not found.
	 * @author Robert Koprinkov
	 * @param id
	 */
	private int getAbilitySlot(int id){
		for(int i=0; i<ability_slots.length; i++){
			if(ability_slots[i]==id)
				return i;
		}
		return -1;
	}

	/**
	 * Handles pressing of one of the ability buttons.
	 * When pressed, the next ability that has not been used by one of the other slots is used.
	 * @param view
	 * @author Robert Koprinkov
	 */
	public void onChangeAbility(View view) {
		int id = view.getId();
		int ability_slot = getAbilitySlot(id);
		int nextab = 0;
		for(int i=0; i<abilities.length; i++){
			if(assigned_to_slot[i]==id){
				nextab = (i+1)%abilities.length;
				assigned_to_slot[i] = 0;
				break;
			}
		}
		Log.e("nextab", nextab + "");
		while(assigned_to_slot[nextab]!=0){
			Log.e("slot", assigned_to_slot[nextab] + " " + nextab);
			nextab = (nextab+1)%assigned_to_slot.length;
		}
		//we have an unassigned ability
		assigned_to_slot[nextab] = id;
		((TextView)findViewById(ability_title_ids[ability_slot])).setText(ability_names[nextab]);
		((TextView)findViewById(ability_description_ids[ability_slot])).setText(ability_descriptions[nextab]);
	}
}
