package com.piper.swishcards

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView


class DeckRecyclerAdapter(context: Context) :
    RecyclerView.Adapter<DeckRecyclerAdapter.DeckViewHolder>() {

    val mContext = context //MainActivity context passed through. Allows MainActivity to run startActivityForResult
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var decks = emptyList<Deck>() //Cached deck for passing livedata

    override fun getItemCount() = decks.size

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val item = decks[position]

        holder.apply {

            //Creating new checkBox listener, thus, set it to null.
            checkBox.setOnClickListener(null)

            bind(item)

            //manages individual onClick (set here to allow prioritising different decks, i.e. dates)
            layout.setOnLongClickListener {view ->
                val intent = Intent(view.context, AddDeckActivity::class.java).apply {
                    putExtra(DeckPassedItemKey, item)
                }
                //cast to MainActivity since it is observing any changes to Decks. Therefore, it will deal with change.
                (mContext as MainActivity).startActivityForResult(intent, AddDeckActivityStartForResult)
                true
            }

            //Open FlashCard Overview
            layout.setOnClickListener { view ->
                val intent: Intent = Intent(view.context, FlashCardsOverview::class.java).apply {
                    putExtra(passDeckToFlashCardOverview, item)
                }
                view.context.startActivity(intent)
            }


            //send Broadcast to MainActivity for globalViewModel to update the completed parametre for Deck (LocalBroadcastManager for security purposes)
            //isue in non-complete where if ticked to hide it will also automatically check the next box
            checkBox.setOnCheckedChangeListener { checkbox: CompoundButton, _isChecked: Boolean ->
                item.completed = _isChecked
                val intent = Intent().apply {
                    setAction(changeCompletedForDeck)
                    putExtra(changeCompletedForDeckItemID, item)
                }
                //Update the status of the Deck immediately. Allows for sorting to occur properly.
                LocalBroadcastManager.getInstance(checkbox.context).sendBroadcast(intent)
            }

            //After the logic has occurred, than update the UI.
            checkBox.isChecked = item.completed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = inflater.inflate(R.layout.fragment_deck_item, parent, false)
        return DeckViewHolder(view)
    }

    internal fun setDecks(deck: List<Deck>) {
        this.decks = deck
        //Required to update the RecyclerView when the list is reassigned.
        notifyDataSetChanged()
    }

    //ViewHolder
    class DeckViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        //declarations
        private val title: TextView = v.findViewById(R.id.fragment_deck_item_title)
        private val date: TextView = v.findViewById(R.id.fragment_deck_item_due_date)
        val layout: LinearLayout = v.findViewById(R.id.fragment_deck_item_linearLayout)
        val checkBox: CheckBox = v.findViewById(R.id.fragment_deck_item_selected) //reused so will be buggy for items changed

        fun bind(item: Deck) {
            title.text = item.title
            date.text = Deck.getStringFromCalendar(item.date) //Converts the Calender/Date to string
        }

    }
    //constants used for startActivityForResult
    companion object {
        const val AddDeckActivityStartForResult = 1999
        const val DeckPassedItemKey = "deck_passed_from_deck_recycler_view"
        const val changeCompletedForDeck = "deck_passed_changing_boolean_for_completed"
        const val passDeckToFlashCardOverview = "deck_transporting_to_flash_card_overview"
        const val changeCompletedForDeckItemID = "deck_passed_as_item_for_changing_completed_boolean"
    }
}

