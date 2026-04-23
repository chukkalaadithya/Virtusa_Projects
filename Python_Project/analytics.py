from collections import defaultdict
import matplotlib.pyplot as plt


def category_summary(expenses, month):
    summary = defaultdict(float)
    for e in expenses:
        if e["date"].startswith(month):
            summary[e["category"]] += e["amount"]
    return summary


def monthly_summary(expenses, month):
    filtered = [e for e in expenses if e["date"].startswith(month)]

    if not filtered:
        return 0, 0

    total = sum(e["amount"] for e in filtered)
    days = len(set(e["date"] for e in filtered))
    avg = total / days if days else 0

    return total, avg


def show_advanced_chart(summary, month):
    if not summary:
        print("No data available for this month.")
        return

    categories = list(summary.keys())
    values = list(summary.values())
    total = sum(values)

    categories, values = zip(*sorted(zip(categories, values),key=lambda x: x[1],reverse=True))
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(15, 6))
    wedges, _, autotexts = ax1.pie(values,autopct='%1.1f%%',startangle=140,pctdistance=0.75,wedgeprops=dict(width=0.4))
    ax1.text(0, 0,f"₹{int(total)}\nTotal",ha='center',va='center',fontsize=12,fontweight='bold')

    ax1.set_title(f"Spending Breakdown — {month}")

    for t in autotexts:
        t.set_fontsize(9)

    legend_labels = [
        f"{c}: ₹{int(v)} ({(v/total)*100:.1f}%)"
        for c, v in zip(categories, values)
    ]
    
    ax1.legend(wedges,legend_labels,loc="center left",bbox_to_anchor=(1, 0.5),fontsize=9)
    max_val = max(values)
    upper = int(max_val * 1.2)
    ax2.barh(categories, values, height=0.5)
    ax2.set_xlim(0, upper)
    ax2.set_title("Category Breakdown")
    ax2.set_xlabel("Amount (₹)")

    for i, v in enumerate(values):
        label = f"₹{int(v)} ({(v/total)*100:.1f}%)"

        if v > upper * 0.15:
            ax2.text(v - upper * 0.05,i,label,va='center',ha='right',color='white',fontsize=9)
        else:
            ax2.text(v + upper * 0.02,i,label,va='center',ha='left',fontsize=9)

    ax2.grid(axis='x', linestyle='--', alpha=0.3)

    plt.tight_layout()
    plt.show()