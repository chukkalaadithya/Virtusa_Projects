from collections import defaultdict
import matplotlib.pyplot as plt


def category_summary(expenses, month):
    summary = defaultdict(float)

    for e in expenses:
        if e["date"].startswith(month):
            summary[e["category"]] += e["amount"]

    return summary


def monthly_summary(expenses, month):
    total = 0
    days = set()

    for e in expenses:
        if e["date"].startswith(month):
            total += e["amount"]
            days.add(e["date"])

    if len(days) == 0:
        return 0, 0

    avg = total / len(days)

    return total, avg


def show_advanced_chart(summary, month):
    if not summary:
        print("No data available.")
        return

    categories = list(summary.keys())
    amounts = list(summary.values())

    plt.figure(figsize=(8, 5))

    bars = plt.bar(categories, amounts)

    for bar in bars:
        height = bar.get_height()

        plt.text(
            bar.get_x() + bar.get_width() / 2,
            height,
            f"₹{int(height)}",
            ha='center',
            va='bottom'
        )

    plt.ylim(0, max(amounts) * 1.15)

    plt.title(f"Expenses - {month}")
    plt.xlabel("Category")
    plt.ylabel("Amount")

    plt.tight_layout()
    plt.show()
