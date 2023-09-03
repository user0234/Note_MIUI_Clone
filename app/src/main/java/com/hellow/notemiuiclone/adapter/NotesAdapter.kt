package com.hellow.notemiuiclone.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.databinding.NotesListItemBinding
import com.hellow.notemiuiclone.models.NoteDescItemType
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.utils.Utils


class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    inner class NotesViewHolder(val binding: NotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    // for faster change in view
    private val differCallBack = object : DiffUtil.ItemCallback<NoteItem>() {
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.idDate == newItem.idDate
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.idDate == newItem.idDate && oldItem.recentChangeDate == newItem.recentChangeDate
        }

    }

    val notesDiffer = AsyncListDiffer(this, differCallBack)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NotesAdapter.NotesViewHolder {
        return NotesViewHolder(
            NotesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotesAdapter.NotesViewHolder, position: Int) {

        val currentItem = notesDiffer.currentList[position]
        holder.binding.tvTitle.text = currentItem.title

//        val holderRes = holder.itemView.context.resources
//        val displayDensity = holderRes.displayMetrics.density

        var noteDesc = ""
        for (value in currentItem.noteDescription) {

            if ((value.type == NoteDescItemType.Text || value.type == NoteDescItemType.CheckBox) && value.text != "")
                noteDesc = value.text.toString()
            break

        }
        holder.binding.tvSubtitle.text = noteDesc
        holder.binding.tvTime.text = Utils.dateConvert(currentItem.recentChangeDate)
        holder.binding.root.setCardBackgroundColor(
            (Color.parseColor(
                Utils.backgroundColor(
                    currentItem.themeId
                )
            ))
        )
        holder.binding.tvTime.setTextColor((Color.parseColor(
            Utils.subTitleColor(
                currentItem.themeId
            )
        )))

        holder.binding.tvTitle.setTextColor(
            (Color.parseColor(
                Utils.titleColor(
                    currentItem.themeId
                )
            ))
        )

        holder.binding.tvSubtitle.setTextColor(
            (Color.parseColor(
                Utils.subTitleColor(
                    currentItem.themeId
                )
            ))
        )
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(currentItem)
            }
        }
        /*
//        val radiusClickedDp = (holderRes.getDimension(
//            R.dimen.note_list_item_card_corner_radius_unClicked
//        )) / displayDensity

//        val radius =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radiusClickedDp, holderRes.displayMetrics)

//        if(currentItem.isClicked){
//            holder.binding.root.radius = radius
//        }
        */

        holder.itemView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {

                    MotionEvent.ACTION_DOWN -> {

                        holder.itemView.animate()
                            .scaleY(0.83F)
                            .scaleX(0.83F)
                            .setDuration(70)
                            .start()
/*

//                        currentItem.isClicked = true
//                        // root item setup
//                        val param =
//                            (holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                                setMargins(
//                                    holderRes.getDimensionPixelOffset(R.dimen.note_list_item_card_margin_vertical_clicked),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_card_margin_vertical_clicked
//                                    ),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_card_margin_vertical_clicked
//                                    ),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_card_margin_vertical_clicked
//                                    )
//                                )
//                            }
//                        holder.binding.root.layoutParams = param
//                        holder.binding.root.layoutParams.height =
//                            holderRes.getDimensionPixelOffset(
//                                R.dimen.note_list_item_card_height_clicked
//                            )
//                        holder.binding.root.radius = radius
//
//
//                        // linear layout setup
//                        val llParam =
//                            (holder.binding.llNoteItem.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                                setMargins(
//                                    holderRes.getDimensionPixelOffset(R.dimen.note_list_item_ll_margin_horizontal_clicked),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_ll_margin_top_clicked
//                                    ),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_ll_margin_horizontal_clicked
//                                    ),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_ll_margin_bottom_clicked
//                                    )
//                                )
//                            }
//                        holder.binding.llNoteItem.layoutParams = llParam
//                        // title text setup
//                        holder.binding.tvTitle.setTextSize(
//                            TypedValue.COMPLEX_UNIT_PX,
//                            holderRes.getDimension(
//                                R.dimen.note_list_item_title_size_clicked
//                            )
//                        )
//                        // title sub text setup
//                        holder.binding.tvSubtitle.setTextSize(
//                            TypedValue.COMPLEX_UNIT_PX,
//                            holderRes.getDimension(R.dimen.note_list_item_subTitle_size_clicked)
//                        )
//                        // title date setup
//                        holder.binding.tvTime.setTextSize(
//                            TypedValue.COMPLEX_UNIT_PX,
//                            holderRes.getDimension(R.dimen.note_list_item_date_text_size_clicked)
//                        )
                         */

                    }

                    MotionEvent.ACTION_UP -> {

                        holder.itemView.animate()
                            .scaleX(1F)
                            .scaleY(1F)
                            .setDuration(70)
                            .start()
                          /*
//                        currentItem.isClicked = false
//                        // reset the size of view
//                        // root item setup
//                        val param =
//                            (holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                                setMargins(
//                                    holderRes.getDimensionPixelOffset(R.dimen.note_list_item_card_margin_vertical_unClicked),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_card_margin_vertical_unClicked
//                                    ), holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_card_margin_vertical_unClicked
//                                    ), holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_card_margin_vertical_unClicked
//                                    )
//                                )
//                            }
//                        holder.binding.root.layoutParams = param
//                        holder.binding.root.layoutParams.height =
//                            holderRes.getDimensionPixelOffset(
//                                R.dimen.note_list_item_card_height_unClicked
//                            )
//
//                        holder.binding.root.radius = radius
//
//
//                        // linear layout setup
//                        val llParam =
//                            (holder.binding.llNoteItem.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                                setMargins(
//                                    holderRes.getDimensionPixelOffset(R.dimen.note_list_item_ll_margin_horizontal_unClicked),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_ll_margin_top_unClicked
//                                    ), holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_ll_margin_horizontal_unClicked
//                                    ), holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_ll_margin_bottom_unClicked
//                                    )
//                                )
//                            }
//                        holder.binding.llNoteItem.layoutParams = llParam
//                        // title text setup
//                        holder.binding.tvTitle.setTextSize(
//                            TypedValue.COMPLEX_UNIT_PX,
//                            holderRes.getDimension(
//                                R.dimen.note_list_item_title_size_unClicked
//                            )
//                        )
//                        // title sub text setup
//                        holder.binding.tvSubtitle.setTextSize(
//                            TypedValue.COMPLEX_UNIT_PX,
//                            holderRes.getDimension(R.dimen.note_list_item_subTitle_size_unClicked)
//                        )
//                        // title date setup
//                        holder.binding.tvTime.setTextSize(
//                            TypedValue.COMPLEX_UNIT_PX,
//                            holderRes.getDimension(R.dimen.note_list_item_date_text_size_unClicked)
//                        )
                     */
                    }

                    MotionEvent.ACTION_CANCEL -> {

                        holder.itemView.animate()
                            .scaleX(1F)
                            .scaleY(1F)
                            .setDuration(70)
                            .start()
                            /*
//                        currentItem.isClicked = false
//                        // reset the size of view
//                        // root item setup
//                        val param =
//                            (holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                                setMargins(
//                                    holderRes.getDimensionPixelOffset(R.dimen.note_list_item_card_margin_vertical_unClicked),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_card_margin_vertical_unClicked
//                                    ), holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_card_margin_vertical_unClicked
//                                    ), holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_card_margin_vertical_unClicked
//                                    )
//                                )
//                            }
//                        holder.binding.root.layoutParams = param
//                        holder.binding.root.layoutParams.height =
//                            holderRes.getDimensionPixelOffset(
//                                R.dimen.note_list_item_card_height_unClicked
//                            )
//
//                        holder.binding.root.radius = radius
//
//
//                        // linear layout setup
//                        val llParam =
//                            (holder.binding.llNoteItem.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                                setMargins(
//                                    holderRes.getDimensionPixelOffset(R.dimen.note_list_item_ll_margin_horizontal_unClicked),
//                                    holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_ll_margin_top_unClicked
//                                    ), holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_ll_margin_horizontal_unClicked
//                                    ), holderRes.getDimensionPixelOffset(
//                                        R.dimen.note_list_item_ll_margin_bottom_unClicked
//                                    )
//                                )
//                            }
//                        holder.binding.llNoteItem.layoutParams = llParam
//                        // title text setup
//                        holder.binding.tvTitle.setTextSize(
//                            TypedValue.COMPLEX_UNIT_PX,
//                            holderRes.getDimension(
//                                R.dimen.note_list_item_title_size_unClicked
//                            )
//                        )
//                        // title sub text setup
//                        holder.binding.tvSubtitle.setTextSize(
//                            TypedValue.COMPLEX_UNIT_PX,
//                            holderRes.getDimension(R.dimen.note_list_item_subTitle_size_unClicked)
//                        )
//                        // title date setup
//                        holder.binding.tvTime.setTextSize(
//                            TypedValue.COMPLEX_UNIT_PX,
//                            holderRes.getDimension(R.dimen.note_list_item_date_text_size_unClicked)
//                        )

                             */
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }

        })


    }

    override fun getItemCount(): Int {
        return notesDiffer.currentList.size
    }

    private var onItemClickListener: ((NoteItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (NoteItem) -> Unit) {
        onItemClickListener = listener
    }

}